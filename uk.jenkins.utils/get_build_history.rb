# frozen_string_literal: true
require './dom/base/jenkins_job.rb'
require './dom/build_brunch_history.rb'
def main(rootDir, jobName, brunch)
  JenkinsJob.run(rootDir, jobName) do |job|
    build_history = BuildBrunchHistory.load(job)
    build_history.all_builds(brunch).each do |build|
      puts "##{build.number} :#{build.build_result}"
    end
  end
end

main(ARGV[0], ARGV[1], ARGV[2])
