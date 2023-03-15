# frozen_string_literal: true
require './dom/base/jenkins_job.rb'
require './dom/select_brunch_build.rb'
def main(rootDir, jobName)
  job = JenkinsJob.new(rootDir, jobName)

  if !job.exist?
    puts "指定したジョブは存在しません:#{job.jobName}"
    return
  end

  successBrunches = []

  SelectBrunchBuild.load(job).sort_by{|build| build.number}.each do |build|
    brunch = build.get_brunch

    if(brunch)
      if(build.success?)
        successBrunches << brunch
      else
        if(successBrunches.include?(brunch))
          successBrunches.delete(brunch)
        end
      end
    end
  end

  successBrunches.uniq.each do |brunch|
    puts(brunch)
  end
end

main(ARGV[0], ARGV[1])
